import React from 'react'
import {
  View,
  Text,
  TouchableOpacity,
  Image,
} from 'react-native'

import { connect } from 'react-redux'
import { actions } from './reducers/compose.js'

import Icon from 'react-native-vector-icons/MaterialCommunityIcons'
import ImagePicker from 'react-native-image-picker'

import ToolBar from './ToolBar.js'
import GiantInput from './components/GiantInput.js'
import ImageButton from './components/ImageButton.js'
import ComposeThumbnail from './components/ComposeThumbnail.js'
import ImagePreviewOverlay from './components/ImagePreviewOverlay.js'

const mapStateToProps = (state) => {
  return {
    draft: state.compose,
    api: state.api.twitter.rest,
  }
}
const mapDispatchToProps = (dispatch) => ({
  onChangeText: (text) => {dispatch({ type: actions.changeText, text: text })},
  sendTweet: async (api, draft) => {
    console.log(draft)
    if (!draft.valid) return
    try {
      const resp = await api.post('statuses/update', {
        status: draft.text,
      })
      console.log(resp)
      dispatch({ type: actions.clear })
    } catch (e) {
      console.log(e)
    }
  },
  pickedImage: (imageUri) => {
    dispatch({ type: actions.pickImage, imageUri: imageUri })
  },
  handlePreviewImage: (imageUri) => {
    dispatch({ type: actions.previewImage, imageUri: imageUri })
  },
  handlePreviewClose: () => {
    dispatch({ type: actions.previewClear })
  },
  dispatch,
})

@connect(mapStateToProps, mapDispatchToProps)
export default class ComposeScreen extends React.Component {
  componentWillMount() {
    this.props.dispatch({ type: actions.clear })
  }

  pickerOptions = {
    noData: true,
    mediaType: 'photo',
    quality: 0.5,
  }

  handleTweetClick = async () => {
    const { api, draft } = this.props
    await this.props.sendTweet(api, draft)
  }

  imagePickCallback = (resp) => {
    if (resp.didCancel || resp.error) {
      return
    }
    console.log(resp)
    this.props.pickedImage(resp.uri)
  }

  handleCamera = () => {
    ImagePicker.launchCamera(this.pickerOptions, this.imagePickCallback)
  }

  handleImagePick = async () => {
    ImagePicker.launchImageLibrary(this.pickerOptions, this.imagePickCallback)
  }

  renderThumbnail = (imageUri, index) => {
    return (
      <ComposeThumbnail
        onPress={this.props.handlePreviewImage}
        uri={imageUri}
        key={index}
      />
    )
  }

  render() {
    console.log(this.props.draft)
    return (
      <View style={styles.container}>
        <ToolBar />
        <View style={styles.inputContainer}>
          <GiantInput
            text={this.props.draft.text}
            onChangeText={this.props.onChangeText}
            count={this.props.draft.count}
          />
        </View>
        <View style={styles.buttonsContainer}>
          <View style={styles.leftButtons}>
            <ImageButton name="camera" onPress={this.handleCamera} />
            <ImageButton name="folder-multiple-image" onPress={this.handleImagePick} />
            {/* <ImageButton name="map-marker" /> */}
            {this.props.draft.images.map(this.renderThumbnail)}
          </View>
          <TouchableOpacity
            style={styles.tweetButton}
            onPress={this.handleTweetClick}
            disabled={!this.props.draft.valid}
          >
            <Icon name="comment" color="white" size={24} />
            <Text style={styles.buttonText}>Tweet</Text>
          </TouchableOpacity>
        </View>
        <ImagePreviewOverlay
          uri={this.props.draft.previewImageUri}
          onClose={this.props.handlePreviewClose}
        />
      </View>
    )
  }
}

const styles = {
  container: {
    flex: 1,
    backgroundColor: '#222',
    flexDirection: 'column',
  },
  inputContainer: {
    flex: 1,
  },
  buttonsContainer: {
    padding: 12,
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  leftButtons: {
    flexDirection: 'row',
  },
  tweetButton: {
    flexDirection: 'row',
    marginRight: 8,
  },
  buttonText: {
    marginLeft: 8,
    color: 'white'
  },
}
