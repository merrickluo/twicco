import React from 'react'
import {
  StyleSheet,
  View,
} from 'react-native'

import { connect } from 'react-redux'

import BaseScreen from './BaseScreen.js'
import TweetList from './components/TweetList.js'

@connect(state => {
  return {
    account: state.app.account,
    api: state.api.twitter,
  }
})
export default class HomeScreen extends BaseScreen {
  constructor() {
    super()
    this.state = {
      tweets: [],
    }
  }
  componentDidMount() {
    this.props.api.rest.get('statuses/home_timeline')
        .then(result => {
          console.log(result[0])
          this.setState({
            tweets: result,
          })
        })
        .catch(e => {
          console.log(e)
        })
  }

  render() {
    return (
      <View style={styles.container}>
        <TweetList tweets={this.state.tweets} />
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
  },
  content: {
    flex: 1,
  },
})
